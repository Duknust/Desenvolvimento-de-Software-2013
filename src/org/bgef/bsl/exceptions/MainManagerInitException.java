/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bgef.bsl.exceptions;

/**
 *
 * @author duarteduarte
 */
public class MainManagerInitException extends Exception {

    public MainManagerInitException(String ficheiro_properties_não_encontrado, Exception ex) {
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
