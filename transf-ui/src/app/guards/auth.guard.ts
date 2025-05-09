import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '@app/services/auth.service';

export const authGuard: CanActivateFn = (route, state) => {
  const router: Router = inject(Router);
  const authenticated = inject(AuthService).isAuthenticated();
  if (!authenticated) {
    return router.navigate(['/login']);
  }
  return true;
};
