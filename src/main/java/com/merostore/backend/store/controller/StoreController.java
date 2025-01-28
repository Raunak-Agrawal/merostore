package com.merostore.backend.store.controller;

import com.merostore.backend.common.MessageResponse;
import com.merostore.backend.common.ResponseDto;
import com.merostore.backend.security.domain.Role.Role;
import com.merostore.backend.security.domain.User;
import com.merostore.backend.security.service.UserService;
import com.merostore.backend.seller.domain.Seller;
import com.merostore.backend.store.dto.StoreCreateDTO;
import com.merostore.backend.store.dto.StoreDTO;
import com.merostore.backend.store.dto.StoreUpdateDTO;
import com.merostore.backend.store.service.StoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.security.Principal;


@RestController
@RequestMapping("stores")
@Slf4j
public class StoreController {
    @Autowired
    private StoreService storeService;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseDto<StoreCreateDTO> createStore(@Valid @RequestBody StoreCreateDTO store, Principal principal) {
        User user = userService.findUserByMobileNumberAndRole(principal.getName(), Role.ROLE_SELLER);

//        service.validateInput(store.getBusinessCategory()); //custom code to check enum in the api request.
//        if (!BusinessCategory.isValid(store.getBusinessCategory())){
//            throw new ApiValidationException("business category not found");
//        }
        StoreCreateDTO newStore = storeService.createStore(store, user.getSeller());
        return ResponseDto.success("Successfully created store", newStore);
    }

    @PutMapping(path = "/{id}")
    public ResponseDto<StoreUpdateDTO> updateStore(@PathVariable Long id, @Valid @RequestBody StoreUpdateDTO store) {
        StoreUpdateDTO updatedStore = storeService.updateStore(store, id);
        return ResponseDto.success("Successfully updated store", updatedStore);
    }

    @DeleteMapping("/{id}")
    public ResponseDto<MessageResponse> deleteStore(@PathVariable Long id) {
        storeService.deleteStore(id);
        return ResponseDto.success("Successfully deleted store");
    }

    @GetMapping("")
    public StoreDTO getStoreBySellerId(Authentication authentication) {

        Long sellerId = userService.findUserByMobileNumberAndRole(authentication.getName(), Role.ROLE_SELLER).getSeller();
        if (sellerId == null) {
            throw new EntityNotFoundException("No seller found");
        }

        return storeService.getFirstStoreForSeller(sellerId);

    }


    /**
     *
     * @param id
     * @param file
     * @return Returns filename of the uploaded file on the specific store folder.
     */
    @PutMapping(path = "/{id}/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseDto<String> uploadImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        String res = storeService.upload(file, id);
        return ResponseDto.success("File uploaded successfully", res);
    }
}
