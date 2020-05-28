import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientXsrfModule, HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ProductsComponent } from './components/products/products.component';
import { CreateProductComponent } from './components/create-product/create-product.component';
import { EditProductComponent } from './components/edit-product/edit-product.component';
import { CategoriesComponent } from './components/categories/categories.component';
import { CreateCategoryComponent } from './components/create-category/create-category.component';
import { EditCategoryComponent } from './components/edit-category/edit-category.component';
import { LoginComponent } from './components/login/login.component';
import { ReactiveFormsModule } from '@angular/forms';
import { ErrorInterceptor } from './interceptors/error.interceptor';

@NgModule({
	declarations: [
		AppComponent,
		ProductsComponent,
		CreateProductComponent,
		EditProductComponent,
		CategoriesComponent,
		CreateCategoryComponent,
		EditCategoryComponent,
		LoginComponent
	],
	imports: [
		BrowserModule,
		AppRoutingModule,
		ReactiveFormsModule,
		HttpClientModule,
		HttpClientXsrfModule.withOptions({
			headerName: 'X-CSRF-TOKEN'
		})
	],
	providers: [
		{ provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true }
	],
	bootstrap: [AppComponent]
})
export class AppModule { }
