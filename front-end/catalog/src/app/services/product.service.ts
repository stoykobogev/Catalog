import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Product } from '../models/product.model';
import { Observable } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class ProductService {

	private readonly BASE_URL = '/api/products'

	constructor(private http: HttpClient) { }

	getProductsByCategoryId(categotyId: number): Observable<Product[]> {
		return this.http.get<Product[]>(this.BASE_URL + '/category/' + categotyId);
	}

	deleteProduct(id: number): Observable<void> {
		return this.http.delete<void>(this.BASE_URL + '/' + id);
	}

	getProductImageUrl(id: number): string {
		return this.BASE_URL + '/' + id + '/image';
	}
}
