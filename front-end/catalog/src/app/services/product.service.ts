import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Product } from '../models/product.model';
import { Observable, Subscription } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class ProductService {

	private readonly BASE_URL = '/api/products'

	constructor(private http: HttpClient) { }

	getProductsByCategoryId(categotyId: number): Observable<Product[]> {
		return this.http.get<Product[]>(this.BASE_URL + '/category/' + categotyId);
	}

	createProduct(params: { categoryId: number; name: string; price: number; image: string }): Observable<number> {
		return this.http.post<number>(this.BASE_URL, params);
	}

	editProduct(id: number, params: { categoryId: number; name: string; price: number; image: string }): Observable<void> {
		return this.http.put<void>(this.BASE_URL + '/' + id, params);
	}

	deleteProduct(id: number): Observable<void> {
		return this.http.delete<void>(this.BASE_URL + '/' + id);
	}

	getProductImageUrl(id: number): string {
		return this.BASE_URL + '/' + id + '/image';
	}

	getProductImage(id: number): Observable<Blob> {
		return this.http.get(this.getProductImageUrl(id), {
			responseType: 'blob'
		});
	}

	getProduct(id: number): Observable<Product> {
		return this.http.get<Product>(this.BASE_URL + '/' + id);
	}
}
