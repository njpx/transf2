import { Directive, Input, TemplateRef, ViewContainerRef } from '@angular/core';
import { AuthService } from '@app/services/auth.service';

@Directive({
  selector: '[hasNotRole]',
  standalone: true,
})
export class HasNotRoleDirective {
  @Input() set hasNotRole(role: string) {
    const userRole = this.authService.getRole();
    if (userRole !== role) {
      this.viewContainer.createEmbeddedView(this.templateRef);
    } else {
      this.viewContainer.clear();
    }
  }

  constructor(
    private templateRef: TemplateRef<any>,
    private viewContainer: ViewContainerRef,
    private authService: AuthService
  ) {}
}
