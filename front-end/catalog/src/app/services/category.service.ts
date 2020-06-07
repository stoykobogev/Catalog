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

	getCategory(id: number): Observable<Category> {
		return this.http.get<Category>(this.CATEGORIES_URL + '/' + id);
	}

	createCategory(name: string): Observable<number> {
		return this.http.post<number>(this.CATEGORIES_URL, { name: name });
	}

	editCategory(id: number, name: string): Observable<void> {
		return this.http.put<void>(this.CATEGORIES_URL + '/' + id, { name: name });
	}

	deleteCategory(id: number): Observable<void> {
		return this.http.delete<void>(this.CATEGORIES_URL + '/' + id);
	}
}
