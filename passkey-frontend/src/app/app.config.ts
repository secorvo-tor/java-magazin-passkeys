import { ApplicationConfig, importProvidersFrom } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { HttpClientModule } from '@angular/common/http';
import { environment } from '../environment/environment';
import { BASE_PATH } from './rest-api';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

export const appConfig: ApplicationConfig = {
    providers: [
        {
            provide: BASE_PATH,
            useValue: environment.restApiBasePath,
        },
        provideRouter(routes),
        importProvidersFrom(HttpClientModule),
        importProvidersFrom([BrowserAnimationsModule])
    ]
};
