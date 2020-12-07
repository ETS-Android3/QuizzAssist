/***
 * LinkingInterface.java
 * Developers: Brandon Yip, Vatsal Parmar, Andrew Yeon
 * CMPT 276 Team 'ForTheStudents'
 * Required interface to enable data transfer from RecyclerViewAdapter.java to
 * HomeFragment.java, or any other fragment in general via bundles
 */

package com.example.quizza;

public interface LinkingInterface {
    void sendData(String value);
}