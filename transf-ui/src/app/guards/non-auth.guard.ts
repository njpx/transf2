import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '@services/auth.service';

export const nonAuthGuard: CanActivateFn = (route, state) => {
  const router: Router = inject(Router);
  const authenticated = inject(AuthService).isAuthenticated();

  if (authenticated) {
    router.navigate(['/dashboard']);
    return false;
  }
  return true;
};
