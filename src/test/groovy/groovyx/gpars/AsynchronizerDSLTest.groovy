// GPars - Groovy Parallel Systems
//
// Copyright © 2008-10  The original author or authors
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//       http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package groovyx.gpars

import java.util.concurrent.Executors
import java.util.concurrent.ThreadFactory
import static groovyx.gpars.GParsExecutorsPool.withExistingPool
import static groovyx.gpars.GParsExecutorsPool.withPool

/**
 * @author Vaclav Pech
 * Date: Nov 23, 2008
 */
public class ThreadPoolDSLTest extends GroovyTestCase {
    public void testDSLInitialization() {
        withPool {
            assert ([2, 4, 6, 8, 10] == [1, 2, 3, 4, 5].collectParallel {it * 2})
            assert [1, 2, 3, 4, 5].everyParallel {it > 0}
            assert [1, 2, 3, 4, 5].findParallel {Number number -> number > 2} in [3, 4, 5]
        }
        withPool(5) {
            assert ([2, 4, 6, 8, 10] == [1, 2, 3, 4, 5].collectParallel {it * 2})
            assert [1, 2, 3, 4, 5].everyParallel {it > 0}
            assert [1, 2, 3, 4, 5].findParallel {Number number -> number > 2} in [3, 4, 5]
        }

        def threadFactory = {Runnable runnable ->
            Thread t = new Thread(runnable)
            t.daemon = false
            return t
        } as ThreadFactory

        withPool(5, threadFactory) {
            assert ([2, 4, 6, 8, 10] == [1, 2, 3, 4, 5].collectParallel {it * 2})
            assert [1, 2, 3, 4, 5].everyParallel {it > 0}
            assert [1, 2, 3, 4, 5].findParallel {Number number -> number > 2} in [3, 4, 5]
        }

        final def pool = Executors.newFixedThreadPool(5)
        withExistingPool(pool) {
            assert ([2, 4, 6, 8, 10] == [1, 2, 3, 4, 5].collectParallel {it * 2})
            assert [1, 2, 3, 4, 5].everyParallel {it > 0}
            assert [1, 2, 3, 4, 5].findParallel {Number number -> number > 2} in [3, 4, 5]
        }
        pool.shutdown()
    }
}
