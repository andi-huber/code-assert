/*
 * Copyright © 2015 Stefan Niederhauser (nidin@gmx.ch)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package guru.nidi.codeassert.config;

import java.util.List;

import static guru.nidi.codeassert.util.ListUtils.andJoin;
import static java.util.Arrays.asList;

public final class CollectorConfig<A extends Action> {
    public final String reason;
    public final boolean ignoreUnused;
    public final List<A> actions;

    private CollectorConfig(String reason, boolean ignoreUnused, List<A> actions) {
        this.reason = reason;
        this.ignoreUnused = ignoreUnused;
        this.actions = actions;
    }

    public CollectorConfig<A> ignoringUnused() {
        return new CollectorConfig<>(reason, true, actions);
    }

    @SafeVarargs
    public static <A extends Action> CollectorConfig<A> because(String reason, A... actions) {
        return new CollectorConfig<>(reason, false, asList(actions));
    }

    @SafeVarargs
    public static <A extends Action> CollectorConfig<A> just(A... actions) {
        return because(null, actions);
    }

    @Override
    public String toString() {
        return andJoin("  " + (reason == null ? "just" : ("because " + reason)), actions);
    }
}
