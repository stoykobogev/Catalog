import { Injectable } from '@angular/core';
import { User } from '../models/user.model';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { tap } from 'rxjs/operators';


@Injectable({
	providedIn: 'root'
})
export class AuthService {

	private readonly SESSION_STORAGE_USER_KEY = 'catalog-user';

	currentUser = new BehaviorSubject<User>(null);

	constructor(private http: HttpClient) {

		const userJson = window.sessionStorage.getItem(this.SESSION_STORAGE_USER_KEY);

		if (userJson) {
			this.currentUser.next(JSON.parse(userJson));
		}
	}

	login(username: string, password: string): Observable<User> {
		return this.http.post<User>('/api/login', null, {
			headers: {
				'Content-Type': 'application/x-www-form-urlencoded'
			},
			params: {
				username: username,
				password: password
			}
		})
		.pipe(tap((user) => {
			this.currentUser.next(user);
			window.sessionStorage.setItem(this.SESSION_STORAGE_USER_KEY, JSON.stringify(user));
		}));
	}

	logout(): void {
		this.http.post<void>('/api/logout', null).subscribe();
		this.currentUser.next(null);
		window.sessionStorage.removeItem(this.SESSION_STORAGE_USER_KEY);
	}
}
