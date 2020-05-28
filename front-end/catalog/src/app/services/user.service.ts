import { Injectable } from '@angular/core';
import { User } from '../models/user.model';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { tap } from 'rxjs/operators';

@Injectable({
	providedIn: 'root'
})
export class UserService {

	currentUser = new BehaviorSubject<User>(null);

	constructor(private http: HttpClient) { }

	login(username: string, password: string): Observable<User> {
		return this.http.post<User>('/api/login', { username: username, password: password})
			.pipe(tap((user) => {
				this.currentUser.next(user);
			}));
	}

	logout(): void {
		this.http.get<void>('/api/logout').subscribe();
		this.currentUser.next(null);
	}
}
