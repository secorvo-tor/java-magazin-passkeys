import { Injectable } from '@angular/core';
import { create, RegistrationPublicKeyCredential, } from '@github/webauthn-json/browser-ponyfill';


@Injectable({
    providedIn: 'root',
})
export class WebauthnService {

    async createCredentials(options: CredentialCreationOptions): Promise<RegistrationPublicKeyCredential> {
        return await create(options);
    }
}
