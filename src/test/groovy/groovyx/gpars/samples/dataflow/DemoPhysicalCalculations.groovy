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

package groovyx.gpars.samples.dataflow

import groovyx.gpars.dataflow.DataFlowVariable
import static groovyx.gpars.dataflow.DataFlow.task

/**
 * An example showing multiple threads calculating different parts of a complex physical calculation
 * and one thread consolidating the results of individual calculations into a final report.
 */

final def mass = new DataFlowVariable()
final def radius = new DataFlowVariable()
final def volume = new DataFlowVariable()
final def density = new DataFlowVariable()
final def acceleration = new DataFlowVariable()
final def time = new DataFlowVariable()
final def velocity = new DataFlowVariable()
final def decelerationForce = new DataFlowVariable()
final def deceleration = new DataFlowVariable()
final def distance = new DataFlowVariable()
final def author = new DataFlowVariable()

def t = task {
    println """
Calculating distance required to stop a moving ball.
====================================================
The ball has a radius of ${radius.val} meters and is made of a material with ${density.val} kg/m3 density,
which means that the ball has a volume of ${volume.val} m3 and a mass of ${mass.val} kg.
The ball has been accelerating with ${acceleration.val} m/s2 from 0 for ${time.val} seconds and so reached a velocity of ${velocity.val} m/s.

Given our ability to push the ball backwards with a force of ${decelerationForce.val} N (Newton), we can cause a deceleration
of ${deceleration.val} m/s2 and so stop the ball at a distance of ${distance.val} m.

=======================================================================================================================
This example has been calculated asynchronously in multiple threads using GPars DataFlow concurrency in Groovy.
Author: ${author.val}
"""

}

task {
    mass << volume.val * density.val
}

task {
    volume << Math.PI * (radius.val ** 3)
}

task {
    radius << 2.5
    density << 998.2071  //water
    acceleration << 9.80665 //free fall
    decelerationForce << 900
}

task {
    println 'Enter your name:'
    def name = new InputStreamReader(System.in).readLine()
    author << (name?.trim()?.size() > 0 ? name : 'anonymous')
}

task {
    time << 10
    velocity << acceleration.val * time.val
}

task {
    deceleration << decelerationForce.val / mass.val
}

task {
    distance << deceleration.val * ((velocity.val / deceleration.val) ** 2) * 0.5
}

t.join()