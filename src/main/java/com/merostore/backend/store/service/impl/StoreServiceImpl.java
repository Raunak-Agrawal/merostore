package com.merostore.backend.store.service.impl;

import com.merostore.backend.config.BucketName;
import com.merostore.backend.exception.AssetNotFoundException;
import com.merostore.backend.seller.domain.Seller;
import com.merostore.backend.seller.repository.SellerRepository;
import com.merostore.backend.store.domain.Store;
import com.merostore.backend.store.dto.StoreCreateDTO;
import com.merostore.backend.store.dto.StoreDTO;
import com.merostore.backend.store.dto.StoreUpdateDTO;
import com.merostore.backend.store.repository.StoreRepository;
import com.merostore.backend.store.service.StoreService;
import com.merostore.backend.utils.services.FileStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static org.apache.http.entity.ContentType.*;


@Slf4j
@Service
@Transactional
public class StoreServiceImpl implements StoreService {
    @Autowired
    StoreRepository storeRepository;

    @Autowired
    SellerRepository sellerRepository;

    @Autowired
    private FileStore fileStore;

    @Override
    public StoreCreateDTO createStore(StoreCreateDTO storeRequest, Long sellerId) {
        Store store = Store.builder()
                .name(storeRequest.getName())
                .businessCategory(storeRequest.getBusinessCategory())
                .city(storeRequest.getCity())
                .image(fileStore.getPlaceholderImageURI())
                .address(storeRequest.getAddress())
                .link(generateStoreURL(storeRequest.getName())) //Generate store URL for the first time
                .seller(sellerRepository.findById(sellerId).get())
                .build();

        Store createdStore = storeRepository.save(store);

        return StoreCreateDTO.builder()
                .id(createdStore.getId())
                .name(createdStore.getName())
                .businessCategory(createdStore.getBusinessCategory())
                .city(createdStore.getCity())
                .image(createdStore.getImage())
                .address(createdStore.getAddress())
                .link(createdStore.getLink())
                .sellerId(createdStore.getSeller())
                .build();
    }

    @Override
    public Boolean deleteStore(Long id) {
        log.info("Deleting store: {}", id);
        Store store = findById(id);
        store.setIsDeleted(true);
        store.setActive(false);

        // store is deleted, mark all products and category delete.
        store.getProducts().stream().forEach(product -> {
            product.setIsDeleted(true);
            product.setActive(false);
        });
        store.getCategories().stream().forEach(category -> {
            category.setIsDeleted(true);
            category.setActive(false);
        });

        storeRepository.save(store);
        return true;
    }

    @Override
    public Store findById(Long id) {
        Store store = storeRepository.findByIdAndIsDeleted(id, false);
        if (store == null) {
            throw new AssetNotFoundException("Store not found");
        }
        return store;
    }

    @Override
    public StoreUpdateDTO updateStore(StoreUpdateDTO storeRequest, Long id) {

        Store store = findById(id);

        if (!storeRequest.getLink().equalsIgnoreCase(store.getLink())) {
            log.info("Request link, Store link :{}, {}", storeRequest.getLink(), store.getLink());
            Boolean isLinkValid = checkValidity(storeRequest.getLink());
            if (!isLinkValid) {
                throw new AssetNotFoundException("Store URL is already taken");
            }
        }

        store.setName(storeRequest.getName());
        store.setBusinessCategory(storeRequest.getBusinessCategory());
        store.setCity(storeRequest.getCity());
        store.setAddress(storeRequest.getAddress());
        store.setLink(storeRequest.getLink());
        store.setActive(storeRequest.getActive());

        if (!storeRequest.getImage().isEmpty()) {
            store.setImage(storeRequest.getImage());
        } else {
            store.setImage(fileStore.getPlaceholderImageURI());
        }

        if (!storeRequest.getActive().equals(store.getActive())) {
            // store is made inactive, make all products, category inactive.
            store.getProducts().stream().forEach(product -> {
                product.setActive(storeRequest.getActive());
            });

            store.getCategories().stream().forEach(category -> {
                category.setActive(storeRequest.getActive());
            });
        }

        Store updatedStore = storeRepository.save(store);
        log.info("Updating store: {}", updatedStore);

        return StoreUpdateDTO.builder()
                .id(updatedStore.getId())
                .name(updatedStore.getName())
                .businessCategory(updatedStore.getBusinessCategory())
                .city(updatedStore.getCity())
                .address(updatedStore.getAddress())
                .link(updatedStore.getLink())
                .image(updatedStore.getImage())
                .active(updatedStore.getActive())
                .isDeleted(updatedStore.getIsDeleted())
                .sellerId(updatedStore.getSeller())
                .build();

    }

    @Override
    public String upload(MultipartFile file, Long id) {
        //check if the file is empty
        if (file.isEmpty()) {
            throw new IllegalStateException("Cannot upload empty file");
        }
        //Check if the file is an image
        if (!Arrays.asList(IMAGE_PNG.getMimeType(),
                IMAGE_BMP.getMimeType(),
                IMAGE_GIF.getMimeType(),
                IMAGE_JPEG.getMimeType()).contains(file.getContentType())) {
            throw new IllegalStateException("File uploaded is not an image");
        }
        //get file metadata
        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));
        //Save Image in S3
        String path = String.format("%s/%s", BucketName.MEROSTORE.getBucketName(), id);
        String fileExt = file.getOriginalFilename().split("\\.")[1];
        String fileName = String.format("%s.%s", UUID.randomUUID(), fileExt);

        try {
            log.info("Uploading file :{}", fileName);
            fileStore.upload(path, fileName, metadata, file.getInputStream());
        } catch (IOException e) {
            throw new IllegalStateException("Failed to upload file", e);
        }

        return  fileStore.getObjectURI(id, fileName);
    }

    @Override
    public StoreDTO getFirstStoreForSeller(Long sellerId) {
        Store store = storeRepository.findFirstBySellerIdAndIsDeleted(sellerId, false);
        if (store == null) {
            throw new AssetNotFoundException("Store not found or is either deleted");
        }
        return StoreDTO.builder()
                .id(store.getId())
                .name(store.getName())
                .image(store.getImage())
                .link(store.getLink())
                .businessCategory(store.getBusinessCategory())
                .city(store.getCity())
                .address(store.getAddress())
                .isDeleted(store.getIsDeleted())
                .active(store.getActive())
                .sellerId(store.getSeller())
                .build();
    }

    /**
     * @param link
     * @return Check if link is valid for the store
     */
    private Boolean checkValidity(String link) {
        Store store = storeRepository.findByLink(link);
        return store == null;
    }

    /**
     * @param storeName
     * @return Generates storeURL for the given store name.
     */
    private String generateStoreURL(String storeName) {
        //ganpaticollection
        //ganpati collection
        if (storeName == null) {
            return null;
        }
        Boolean isResultFound = false;
        String result = "";

        storeName = storeName.toLowerCase();
        String[] splitStr = storeName.toLowerCase().trim().split("\\s+");

        if (splitStr.length >= 2) {
            String computedString = splitStr[0] + splitStr[1];
            Boolean isTwoStringValid = checkValidity(computedString);
//            if (isTwoStringValid == false) {
//                Boolean isOneStringValid = checkValidity(splitStr[0]);
//                if (isOneStringValid == true) {
//                    isResultFound = true;
//                    result = splitStr[0];
//                }
//            } else
            if (isTwoStringValid) {
                isResultFound = true;
                result = computedString;
            }
        } else if (splitStr.length == 1) {
            Boolean isValid = checkValidity(splitStr[0]);
            if (isValid == true) {
                isResultFound = true;
                result = splitStr[0];
            }
        }

        // Now loop through possible url we can form with the suffix between 11 and 999.
        if (isResultFound) {
            return result;
        } else {
            String nameToCheck = splitStr.length >= 2 ? splitStr[0] + splitStr[1] : splitStr[0];
            Random random = new Random();
            while (true) {
                int x = random.nextInt(999 - 11) + 11;
                String appendedString = nameToCheck + x;
                Boolean isValid = checkValidity(appendedString);
                if (isValid) {
                    result = appendedString;
                    break;
                } else {
                    continue;
                }
            }
        }
        return result;
    }

}


