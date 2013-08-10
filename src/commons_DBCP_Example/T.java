package commons_DBCP_Example;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.ArrayList;
//
// Here are the dbcp-specific classes.
// Note that they are only used in the setupDataSource
// method. In normal use, your classes interact
// only with the standard JDBC API
//

//
// Here's a simple example of how to use the PoolingDataSource.
//

//
// Note that this example is very similar to the PoolingDriver
// example.  In fact, you could use the same pool in both a
// PoolingDriver and a PoolingDataSource
//

//
// To compile this example, you'll want:
//  * commons-pool-1.5.4.jar
//  * commons-dbcp-1.2.2.jar
//  * j2ee.jar (for the javax.sql classes)
// in your classpath.
//
// To run this example, you'll want:
//  * commons-pool-1.5.6.jar
//  * commons-dbcp-1.3.jar (JDK 1.4-1.5) or commons-dbcp-1.4 (JDK 1.6+)
//  * j2ee.jar (for the javax.sql classes)
//  * the classes for your (underlying) JDBC driver
// in your classpath.
//
// Invoke the class using two arguments:
//  * the connect string for your underlying JDBC driver
//  * the query you'd like to execute
// You'll also want to ensure your underlying JDBC driver
// is registered.  You can use the "jdbc.drivers"
// property to do this.
//
// For example:
//  java -Djdbc.drivers=oracle.jdbc.driver.OracleDriver \
//       -classpath commons-pool-1.5.6.jar:commons-dbcp-1.4.jar:j2ee.jar:oracle-jdbc.jar:. \
//       PoolingDataSourceExample \
//       "jdbc:oracle:thin:scott/tiger@myhost:1521:mysid" \
//       "SELECT * FROM DUAL"
//
public class T {

	static int max = 100;

	public static boolean regular() {
		try{
			for (int i = 0; i < max; i++) {
				DB.INST.queryOneConnection(query(), false);
			}
			DB.INST.shutdown();
		}catch(Exception e){
			e.printStackTrace();
		}
		return true;
	}

	public static String query() {
		String name[] = { "bill", "merry", "bob", "tom", "john", "josh", "grace" };
		int index = ((int) (Math.random() * 100)) % 7;
		return "select * from customer where lower(name) like '%" + name[index] + "%'";
	}

	public static boolean bonecp_pool_non_Threading() {
		try {
			for (int i = 0; i < max; i++) {
				BONECP.INST.query(query(), false);
			}
			BONECP.INST.shutdown();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public static boolean dbcp_pool_non_Threading() {
		try {
			for (int i = 0; i < max; i++) {
				DBCP.INST.query(query(), false);
			}
			DBCP.INST.shutdown();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public static boolean bonecp_pool_Threading() {
		ArrayList<Thread> al = new ArrayList<Thread>();
		for (int i = 0; i < max; i++) {
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						BONECP.INST.query(query(), false);
					} catch (Exception e) {
						e.printStackTrace();
					}

				}

			});
			al.add(t);
		}
		for (Thread t : al) {
			t.start();
		}
		for (Thread t : al) {
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		try {
			BONECP.INST.shutdown();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public static boolean dbcp_pool_Threading() {
		ArrayList<Thread> al = new ArrayList<Thread>();
		for (int i = 0; i < max; i++) {
			Thread t = new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						DBCP.INST.query(query(), false);
					} catch (Exception e) {
						e.printStackTrace();
					}

				}

			});
			al.add(t);
		}
		for (Thread t : al) {
			t.start();
		}
		for (Thread t : al) {
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		try {
			DBCP.INST.shutdown();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

}