import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BackButtonForCardComponent } from './back-button-for-card.component';

describe('BackButtonForCardComponent', () => {
  let component: BackButtonForCardComponent;
  let fixture: ComponentFixture<BackButtonForCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BackButtonForCardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BackButtonForCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
