import { Routes } from '@angular/router';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { LoginComponent } from './components/login/login.component';
import { NewBankAccountComponent } from './components/new-bank-account/new-bank-account.component';
import { RegisterComponent } from './components/register/register.component';
import { authGuard } from './guards/auth.guard';
import { nonAuthGuard } from './guards/non-auth.guard';
import { roleGuard } from './guards/role.guard';
import { Roles } from './shared/role.constant';
import { DepositComponent } from './components/deposit/deposit.component';
import { TransferComponent } from './components/transfer/transfer.component';
import { MyBankAccountComponent } from './components/my-bank-account/my-bank-account.component';

export const routes: Routes = [
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
  {
    path: 'login',
    component: LoginComponent,
    canActivate: [nonAuthGuard],
  },
  {
    path: 'register',
    component: RegisterComponent,
    canActivate: [nonAuthGuard],
  },
  {
    path: 'new-customer',
    component: RegisterComponent,
    canActivate: [authGuard, roleGuard(Roles.TELLER)],
  },
  {
    path: 'dashboard',
    component: DashboardComponent,
    canActivate: [authGuard],
  },
  {
    path: 'new-bank-account',
    component: NewBankAccountComponent,
    canActivate: [authGuard, roleGuard(Roles.TELLER)],
  },
  {
    path: 'deposit',
    component: DepositComponent,
    canActivate: [authGuard, roleGuard(Roles.TELLER)],
  },
  {
    path: 'transfer',
    component: TransferComponent,
    canActivate: [authGuard, roleGuard(Roles.CUSTOMER)],
  },
  {
    path: 'my-bank-account',
    component: MyBankAccountComponent,
    canActivate: [authGuard, roleGuard(Roles.CUSTOMER)],
  },
];
