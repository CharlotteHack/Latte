package com.salad.latte.Objects

import java.sql.Timestamp

data class ClientTransaction(val type :String, val amount :Double, val status :String, val timestamp: Long) {

}