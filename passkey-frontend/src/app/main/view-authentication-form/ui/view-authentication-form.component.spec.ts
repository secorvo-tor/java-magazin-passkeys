import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewAuthenticationFormComponent } from './view-authentication-form.component';
import { provideHttpClient } from '@angular/common/http';

describe('ViewAuthenticationFormComponent', () => {
  let component: ViewAuthenticationFormComponent;
  let fixture: ComponentFixture<ViewAuthenticationFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ViewAuthenticationFormComponent],
        providers: [provideHttpClient()]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ViewAuthenticationFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
