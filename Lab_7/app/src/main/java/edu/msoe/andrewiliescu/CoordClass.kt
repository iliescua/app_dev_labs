/*
 * CoordClass
 * Author: Andrew Iliescu
 * Date: 4/27/21
 * This class is used to create a Coord object that has the latitude and longitude stored.
 * This class was written in Kotlin because it made all of the getters and setters auto generate
 *
 */
package edu.msoe.andrewiliescu

data class CoordClass(var latData:String, var longData:String) {
    var lat:String = latData
    var long:String = longData
}