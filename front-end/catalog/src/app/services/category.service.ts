import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Category } from '../models/category.model';

@Injectable({
	providedIn: 'root'
})
export class CategoryService {

	private readonly CATEGORIES_URL = '/api/categories'

	constructor(private http: HttpClient) { }

	getAllCategories(): Observable<Array<Category>> {
		return this.http.get<Array<Category>>(this.CATEGORIES_URL);
	}

	deleteCategory(id: number): Observable<void> {
		return this.http.delete<void>(this.CATEGORIES_URL + '/' + id);
	}
}
