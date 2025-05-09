import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ButtonModule } from 'primeng/button';
@Component({
  selector: 'app-back-button-for-card',
  imports: [RouterModule, ButtonModule],
  templateUrl: './back-button-for-card.component.html',
  styleUrl: './back-button-for-card.component.scss',
})
export class BackButtonForCardComponent {}
