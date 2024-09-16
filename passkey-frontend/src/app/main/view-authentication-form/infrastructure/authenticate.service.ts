import { Injectable } from '@angular/core';
import { AuthenticationService } from '../../../rest-api';
import { lastValueFrom } from 'rxjs';

@Injectable({
    providedIn: 'root',
})
export class AuthenticateService {


    constructor(private readonly authService: AuthenticationService) {
    }

    startAuthentication(username: string): Promise<string> {
        const response = this.authService.startAuthentication(username);

        return lastValueFrom(response);
    }

    finishAuthentication(credentials: string): Promise<any> {
        const response = this.authService.finishAuthentication(credentials);

        return lastValueFrom(response)
    }
}
