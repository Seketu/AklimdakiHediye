package com.reylortechnology.aklimdakihediye.ObserverClasses

sealed class ForWhoObserver {
    object checkState : ForWhoObserver()
    object none : ForWhoObserver()
}