package de.andrena.passkey_demo.persistence;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.yubico.webauthn.CredentialRepository;
import com.yubico.webauthn.RegisteredCredential;
import com.yubico.webauthn.data.ByteArray;
import com.yubico.webauthn.data.PublicKeyCredentialDescriptor;
import com.yubico.webauthn.data.UserIdentity;

import de.andrena.passkey_demo.model.entities.UserRegistration;

/**
 * @author Christoph Eifert (christoph.eifert@brueckner.com)
 */
@Component
public class CustomCredentialRepository implements CredentialRepository {

    private final Map<String, Set<UserRegistration>> registrations = new ConcurrentHashMap<>();

    @Override
    public Set<PublicKeyCredentialDescriptor> getCredentialIdsForUsername(String username) {
        return registrations.getOrDefault(username, Collections.emptySet())
                            .stream()
                            .map(UserRegistration::getCredentialDescriptor)
                            .collect(Collectors.toSet());
    }

    @Override
    public Optional<ByteArray> getUserHandleForUsername(String username) {
        return registrations.getOrDefault(username, Collections.emptySet())
                            .stream()
                            .map(reg -> reg.getUserIdentity()
                                           .getId())
                            .findFirst();
    }

    @Override
    public Optional<String> getUsernameForUserHandle(ByteArray userHandle) {
        return registrations.values()
                            .stream()
                            .flatMap(Collection::stream)
                            .filter(reg -> userHandle.equals(reg.getUserIdentity()
                                                                .getId()))
                            .findFirst()
                            .map(reg -> reg.getUserIdentity()
                                           .getName());
    }

    @Override
    public Optional<RegisteredCredential> lookup(ByteArray credentialId, ByteArray userHandle) {
        return registrations.values()
                            .stream()
                            .flatMap(Collection::stream)
                            .filter(reg -> userHandle.equals(reg.getUserIdentity()
                                                                .getId()) && credentialId.equals(reg.getCredentialDescriptor()
                                                                                                    .getId()))
                            .findFirst()
                            .map(UserRegistration::getRegisteredCredential);
    }

    @Override
    public Set<RegisteredCredential> lookupAll(ByteArray userHandle) {
        return registrations.values()
                            .stream()
                            .flatMap(Collection::stream)
                            .filter(reg -> userHandle.equals(reg.getUserIdentity()
                                                                .getId()))
                            .map(UserRegistration::getRegisteredCredential)
                            .collect(Collectors.toSet());
    }

    public void addUserRegistration(UserRegistration userRegistration) {
        final String username = userRegistration.getUserIdentity()
                                            .getName();
        if (registrations.containsKey(username)) {
            registrations.get(username).add(userRegistration);
        } else {
            registrations.put(username, Set.of(userRegistration));
        }
    }

    public Optional<UserIdentity> findExistingUser(String name) {
        return registrations.getOrDefault(name, Collections.emptySet())
                            .stream()
                            .findFirst()
                            .map(UserRegistration::getUserIdentity);
    }

    public void updateCredential(String username, RegisteredCredential credential, long signatureCount) {
        // TODO: Implementation
    }
}
