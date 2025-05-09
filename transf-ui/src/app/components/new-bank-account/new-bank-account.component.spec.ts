import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewBankAccountComponent } from './new-bank-account.component';

describe('NewBankAccountComponent', () => {
  let component: NewBankAccountComponent;
  let fixture: ComponentFixture<NewBankAccountComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NewBankAccountComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NewBankAccountComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
