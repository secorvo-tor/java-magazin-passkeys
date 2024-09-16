import { Component } from '@angular/core';
import { InputTextModule } from 'primeng/inputtext';
import { FloatLabelModule } from 'primeng/floatlabel';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { RegisterService } from '../infrastructure/register.service';
import { WebauthnService } from '../infrastructure/webauthn.service';
import { DividerModule } from 'primeng/divider';
import { PanelModule } from 'primeng/panel';
import { RegistrationState } from '../model/RegistrationState';
import { parseCreationOptionsFromJSON, RegistrationPublicKeyCredential } from '@github/webauthn-json/browser-ponyfill';
import { AsyncPipe, NgIf } from '@angular/common';
import { prettyPrintJson } from 'pretty-print-json';

@Component({
    selector: 'view-registration-form',
    standalone: true,
    imports: [
        InputTextModule,
        FloatLabelModule,
        FormsModule,
        ButtonModule,
        DividerModule,
        PanelModule,
        NgIf,
        AsyncPipe,
    ],
    templateUrl: './view-registration-form.component.html',
    styleUrl: './view-registration-form.component.scss'
})
export class ViewRegistrationFormComponent {
    protected readonly RegistrationState = RegistrationState;
    registrationState: RegistrationState = RegistrationState.INITIAL;
    username: string = '';

    authChallenge?: CredentialCreationOptions;
    authChallengeDisplay?: any;

    authCredentials?: RegistrationPublicKeyCredential;
    authFinalResponse: string = '';

    isAuthenticatorAvailable = PublicKeyCredential.isUserVerifyingPlatformAuthenticatorAvailable();

    constructor(
        private readonly registerService: RegisterService,
        private readonly webauthnService: WebauthnService) {
    }

    async onStartRegistration() {
        this.authChallengeDisplay = await this.registerService.startRegistration(this.username);
        this.authChallenge = parseCreationOptionsFromJSON(this.authChallengeDisplay);
        this.registrationState = RegistrationState.CHALLENGE;
    }

    async onCreateCredentials() {
        this.authCredentials = await this.webauthnService.createCredentials(this.authChallenge!);
        this.registrationState = RegistrationState.FINISH;
    }

    async onFinishRegistration() {
        this.registerService.finishRegistration(JSON.stringify(this.authCredentials)).then(response => {
            this.authFinalResponse = response;
            this.registrationState = RegistrationState.FINAL;
        }).catch(msg => console.log(msg));
    }

    prettifyJson(jsonObject: any): string {
        return prettyPrintJson.toHtml(jsonObject, { quoteKeys: true });
    }
}
