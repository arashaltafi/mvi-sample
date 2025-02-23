package com.arash.altafi.mvisample.utils.ext

fun Any.getHashLong(): Long {
    return this.hashCode().toLong()
}

fun Any.getHashInt(): Int {
    return this.hashCode()
}
