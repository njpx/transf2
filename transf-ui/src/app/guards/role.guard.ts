import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '@app/services/auth.service';

export const roleGuard = (requiredRole: string): CanActivateFn => {
  return (route, state) => {
    const router: Router = inject(Router);
    const authService = inject(AuthService);

    const userRole = authService.getRole();

    if (userRole !== requiredRole) {
      return router.navigate(['/dashboard']);
    }

    return true;
  };
};
