import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewRegistrationFormComponent } from './view-registration-form.component';
import { provideHttpClient } from '@angular/common/http';

describe('ViewRegistrationFormComponent', () => {
  let component: ViewRegistrationFormComponent;
  let fixture: ComponentFixture<ViewRegistrationFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
        imports: [ViewRegistrationFormComponent],
        providers: [
            provideHttpClient()
        ]
    })
    .compileComponents();

      fixture = TestBed.createComponent(ViewRegistrationFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
