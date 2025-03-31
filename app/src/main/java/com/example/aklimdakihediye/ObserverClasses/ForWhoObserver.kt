package com.example.aklimdakihediye.ObserverClasses

import com.example.aklimdakihediye.LocalDatabase.Models.LocalUserInformation

sealed class ForWhoObserver {
    object checkState : ForWhoObserver()
    object none : ForWhoObserver()
}