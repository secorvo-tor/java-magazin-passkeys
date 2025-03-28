import { Component } from '@angular/core';
import { ButtonDirective } from 'primeng/button';
import { FloatLabelModule } from 'primeng/floatlabel';
import { InputTextModule } from 'primeng/inputtext';
import { PaginatorModule } from 'primeng/paginator';
import { DividerModule } from 'primeng/divider';
import { NgIf } from '@angular/common';
import { PanelModule } from 'primeng/panel';
import { AuthenticationState } from '../model/AuthenticationState';
import { AuthenticateService } from '../infrastructure/authenticate.service';
import {
    AuthenticationPublicKeyCredential,
    CredentialRequestOptionsJSON,
    get,
    parseRequestOptionsFromJSON
} from '@github/webauthn-json/browser-ponyfill';
import { prettyPrintJson } from 'pretty-print-json';

@Component({
  selector: 'view-authentication-form',
  standalone: true,
    imports: [
        ButtonDirective,
        FloatLabelModule,
        InputTextModule,
        PaginatorModule,
        DividerModule,
        NgIf,
        PanelModule
    ],
  templateUrl: './view-authentication-form.component.html',
  styleUrl: './view-authentication-form.component.scss'
})
export class ViewAuthenticationFormComponent {

    protected readonly AuthenticationState = AuthenticationState;

    username: string = '';
    authenticationState: AuthenticationState = AuthenticationState.INITIAL;

    challengeDisplay?: any;
    challenge?: CredentialRequestOptions;
    credentials?: AuthenticationPublicKeyCredential;
    finalResponse: string = '';


    constructor(private readonly authService: AuthenticateService) {
    }

    async onStartAuthentication() {
        this.challengeDisplay = await this.authService.startAuthentication(this.username);
        this.challenge = parseRequestOptionsFromJSON(this.challengeDisplay as CredentialRequestOptionsJSON);
        this.authenticationState = AuthenticationState.CHALLENGE;
    }

    prettifyJson(jsonObject: any): string {
        return prettyPrintJson.toHtml(jsonObject, { quoteKeys: true});
    }

    async onFetchCredentials() {
        var chal = <PublicKeyCredentialRequestOptions> {
            allowCredentials: this.challenge?.publicKey?.allowCredentials,
            challenge: this.challenge?.publicKey?.challenge,
            rpId: this.challenge?.publicKey?.rpId,
            hints: ['client-device']
        }
        var options = <CredentialRequestOptions> {
            publicKey: chal
        }
        this.credentials = await get(options);
        this.authenticationState = AuthenticationState.RETRIEVE_CREDENTIALS;
    }

    async onFinishAuthentication() {
        this.finalResponse = await this.authService.finishAuthentication(JSON.stringify(this.credentials));
        this.authenticationState = AuthenticationState.FINISH;
    }
}
