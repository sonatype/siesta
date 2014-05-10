<!--

    Copyright (c) 2007-2014 Sonatype, Inc. All rights reserved.

    This program is licensed to you under the Apache License Version 2.0,
    and you may not use this file except in compliance with the Apache License Version 2.0.
    You may obtain a copy of the Apache License Version 2.0 at http://www.apache.org/licenses/LICENSE-2.0.

    Unless required by applicable law or agreed to in writing,
    software distributed under the Apache License Version 2.0 is distributed on an
    "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the Apache License Version 2.0 for the specific language governing permissions and limitations there under.

-->
# Description

Sonatype Siesta; JAX-RS Integration

Based on [JBoss RESTEasy 3.x](http://resteasy.jboss.org/) with [Eclipse Sisu](http://eclipse.org/sisu) IoC support.

ATM only javax.validation 1.0 is supported via Apache BVal (with Guice integration).

Some issues with RESTEasy integration requiring CDI which prevents usage atm of the
RESTEasy javax.validation 1.1 features w/o further integration or modification to work around.