/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.donnees;

import java.util.HashMap;

/**
 * cette classe encode une bijection entre sous-ensembles finis de K et V
 * @author Ancmin
 */
class Bijection<K, V>  {
    /**
     * fonction f (qui est une bijection)
     */
    private HashMap<K, V> f = new HashMap<K, V>();

    /**
     * fonction f^{-1} (f inverse)
     */
    private HashMap<V, K> finv = new HashMap<V, K>();


    /**
     * ajoute une correspondance key, f(key)
     * @param key
     * @param keyImage
     */
    public void put(K key, V keyImage)
    {
        f.put(key, keyImage);
        finv.put(keyImage, key);
    }


    /**
     *
     * @param key
     * @return f(key)
     */
    public V getImage(K key)
    {
        return f.get(key);
    }


    /**
     *
     * @param keyImage
     * @return f^{-1}(keyImage)
     */
    public K getAntecedant(V keyImage)
    {
        return finv.get(keyImage);
    }

}
