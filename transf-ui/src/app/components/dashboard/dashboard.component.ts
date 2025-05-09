import { Component, inject, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { HasRoleDirective } from '@app/directives/has-role.directive';
import { AuthService } from '@app/services/auth.service';
import { UserService } from '@app/services/user.service';
import { ErrorResponse } from '@app/shared/error-response';
import { Roles } from '@app/shared/role.constant';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
@Component({
  selector: 'app-dashboard',
  imports: [RouterModule, CardModule, ButtonModule, HasRoleDirective],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss',
})
export class DashboardComponent implements OnInit {
  Roles = Roles;
  message = '';
  englishName: string = '';
  role: string = '';

  private authService = inject(AuthService);
  private userService = inject(UserService);
  private router = inject(Router);

  ngOnInit() {
    this.userService.getUserDetail().subscribe({
      next: (response) => {
        this.englishName = response.englishName;
        this.role = response.role;
      },
      error: (error: ErrorResponse) => {
        switch (error.error.errorCode) {
          case 'TOKEN_EXPIRED_ERROR':
          case 'USER_NOT_FOUND_ERROR':
            this.authService.logout();
            this.router.navigate(['/login']);
            break;
          default:
            this.message = 'Unexpected Error!';
            break;
        }
      },
    });
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
