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

	createProduct(params: { categoryId: number; name: string; price: number; image: any }): Observable<number> {
 
		// setting the outer observable's observer to the http observer makes it complete 
		// since http observables complete after the request is done (tested)
		return new Observable<number>((observer) => {

			const fileReader = new FileReader();

			fileReader.onload = (ev: ProgressEvent<FileReader>) => {
	
				const result = ev.target.result as string;
				params.image = result.slice(result.indexOf('base64,') + 7);
	
				this.http.post(this.BASE_URL, params).subscribe(observer);
			};
	
			fileReader.readAsDataURL(params.image);

			return new Subscription(() => {
				fileReader.abort();
			});
		});
	}

	deleteProduct(id: number): Observable<void> {
		return this.http.delete<void>(this.BASE_URL + '/' + id);
	}

	getProductImageUrl(id: number): string {
		return this.BASE_URL + '/' + id + '/image';
	}
}
