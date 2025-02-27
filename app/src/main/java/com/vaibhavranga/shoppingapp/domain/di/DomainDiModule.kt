package com.vaibhavranga.shoppingapp.domain.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.vaibhavranga.shoppingapp.data.repositoryImpl.RepositoryImpl
import com.vaibhavranga.shoppingapp.domain.repository.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainDiModule {
    @Provides
    @Singleton
    fun provideRepository(
        firebaseAuth: FirebaseAuth,
        firebaseFirestore: FirebaseFirestore,
        firebaseMessaging: FirebaseMessaging
    ): Repository = RepositoryImpl(firebaseAuth, firebaseFirestore, firebaseMessaging)
}