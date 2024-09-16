import { Component } from '@angular/core';
import { TabViewModule } from 'primeng/tabview';
import { ViewRegistrationFormComponent } from '../view-registration-form/ui/view-registration-form.component';
import { ViewAuthenticationFormComponent } from '../view-authentication-form/ui/view-authentication-form.component';

@Component({
  selector: 'app-view-tabs',
  standalone: true,
    imports: [
        TabViewModule,
        ViewRegistrationFormComponent,
        ViewAuthenticationFormComponent
    ],
  templateUrl: './view-tabs.component.html',
  styleUrl: './view-tabs.component.scss'
})
export class ViewTabsComponent {

}
