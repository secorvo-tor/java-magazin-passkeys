import {Injectable} from '@angular/core';
import {RegistrationService} from '../../../rest-api';
import {lastValueFrom} from 'rxjs';


@Injectable({
    providedIn: 'root',
})
export class RegisterService {


    constructor(private readonly httpClient: RegistrationService) {
    }

    startRegistration(username: string): Promise<string> {
        let response = this.httpClient.startRegistration(username);

        return lastValueFrom(response)
    }

    finishRegistration(credentials: string): Promise<any> {
        const response = this.httpClient.finishRegistration(credentials)

        return lastValueFrom(response)
    }
}
