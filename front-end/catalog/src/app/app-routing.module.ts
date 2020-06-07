import { NgModule } from '@angular/core';
import { Routes, RouterModule, ChildActivationEnd } from '@angular/router';
import { AuthGuard } from './auth/auth.guard';
import { Roles } from './constants/roles';
import { CategoriesComponent } from './components/categories/categories.component';
import { ProductsComponent } from './components/products/products.component';
import { CreateProductComponent } from './components/create-product/create-product.component';
import { EditProductComponent } from './components/edit-product/edit-product.component';
import { CreateCategoryComponent } from './components/create-category/create-category.component';
import { EditCategoryComponent } from './components/edit-category/edit-category.component';
import { LoginComponent } from './components/login/login.component';


const routes: Routes = [
	{
		path: 'categories',
		canActivate: [AuthGuard],
		canActivateChild: [AuthGuard],
		children: [
			{
				path: '',
				component: CategoriesComponent,
			},
			{
				path: ':categoryId/products',
				children: [
					{
						path: '',
						canActivate: [AuthGuard],
						component: ProductsComponent,
					},
					{
						path: 'create',
						component: CreateProductComponent,
						canActivate: [AuthGuard],
						data: { roles: [Roles.ADMIN] }
					},
				]
			},
			{
				path: 'create',
				component: CreateCategoryComponent,
				canActivate: [AuthGuard],
				data: { roles: [Roles.ADMIN] }
			},
			{
				path: 'edit/:categoryId',
				component: EditCategoryComponent,
				canActivate: [AuthGuard],
				data: { roles: [Roles.ADMIN] }
			}
		]
	},
	{
		path: 'products/edit/:productId',
		component: EditProductComponent,
		canActivate: [AuthGuard],
		data: { roles: [Roles.ADMIN] }
	},
	{
		path: 'login',
		component: LoginComponent
	},
	{
		path: '**',
		redirectTo: 'categories'
	}
];

@NgModule({
	imports: [RouterModule.forRoot(routes)],
	exports: [RouterModule]
})
export class AppRoutingModule { }
