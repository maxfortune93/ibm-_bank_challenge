import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NavTemplateComponent } from './nav-template.component';

describe('NavTemplateComponent', () => {
  let component: NavTemplateComponent;
  let fixture: ComponentFixture<NavTemplateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [NavTemplateComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(NavTemplateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
