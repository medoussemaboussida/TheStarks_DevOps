import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http'; // Ajoute ceci
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { EtudiantListComponent } from './etudiant-list/etudiant-list.component';

@NgModule({
  declarations: [
    AppComponent,
    EtudiantListComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
