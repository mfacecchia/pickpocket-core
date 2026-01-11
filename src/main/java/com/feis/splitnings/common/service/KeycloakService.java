package com.feis.splitnings.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.feis.splitnings.common.enums.KeycloakUserAttribute;
import com.feis.splitnings.common.exception.ResourceNotFoundException;

import jakarta.ws.rs.NotFoundException;

@Service
public class KeycloakService {
    @Autowired
    private Keycloak keycloak;

    @Value("${splitnings.keycloak.realm-name}")
    private String realmName;

    private final static Logger logger = LogManager.getLogger(KeycloakService.class);

    public UserRepresentation upsertUserAttributes(String externalId, Map<KeycloakUserAttribute, List<String>> attributes) {
        try {
            RealmResource realm = keycloak.realm(realmName);

            UserResource userResource = realm.users().get(externalId);

            UserRepresentation kcUser = userResource.toRepresentation();

            Map<String, List<String>> kcUserAttributes = kcUser.getAttributes() != null ?
                    kcUser.getAttributes()
                    : new HashMap<>();

            // Adding attributes provided by user to already existing user attributes
            attributes.forEach((key, value) -> {
                kcUserAttributes.put(key.getLabel(), value);
            });

            kcUser.setAttributes(kcUserAttributes);
            userResource.update(kcUser);

            logger.info("UpsertUserAttributes ::: Updated keycloak user \"{}\" attributes", externalId);

            return kcUser;
        } catch (NotFoundException e) {
            logger.error("UpsertUserAttributes ::: User with id {} not found", externalId, e);
            throw new ResourceNotFoundException("User", externalId);
        }
    }
}
