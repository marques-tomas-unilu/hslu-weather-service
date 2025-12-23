package ch.hslu.swde.wda.persister.util;



/*******************************************************************************
 * Copyright 2021 Jordan Sucur, HSLU Informatik, Switzerland
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Die Hilfsklasse JpaUtil stellt zwei Klassenmethoden zur Verfügung: eine, die eine EntityManagerFactory
 * zur Verfügung stellt, und eine, die Instanzen des EntityManagers herstellt. 
 *
 */
public class JpaUtil {

    private JpaUtil() {
        // nichts machen
    }

    private static EntityManagerFactory entityManagerFactory = null;

    private static String determinePersistenceUnit(){
        try {
            String environment = System.getProperty("APP_ENV");
            if ("test".equalsIgnoreCase(environment)) {
                return "test-persistence-unit";
            } else {
                return "prod-persistence-unit";
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Returns a new entity manager instace.
     * @return  entity manager
     */
    public static EntityManager createEntityManager() {
        entityManagerFactory = Persistence.createEntityManagerFactory(determinePersistenceUnit());
        return entityManagerFactory.createEntityManager();
    }

}
