import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProfileThemesComponent } from './profile-themes.component';

describe('ProfileThemesComponent', () => {
  let component: ProfileThemesComponent;
  let fixture: ComponentFixture<ProfileThemesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ProfileThemesComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProfileThemesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
