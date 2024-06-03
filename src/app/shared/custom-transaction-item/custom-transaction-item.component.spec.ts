import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CustomTransactionItemComponent } from './custom-transaction-item.component';

describe('CustomTransactionItemComponent', () => {
  let component: CustomTransactionItemComponent;
  let fixture: ComponentFixture<CustomTransactionItemComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CustomTransactionItemComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CustomTransactionItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
