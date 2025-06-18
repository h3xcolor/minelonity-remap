// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.q;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import lombok.Generated;

@Environment(value = EnvType.CLIENT)
public final class Pair<F, S> {
    private final F first;
    private final S second;

    @Generated
    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    @Generated
    public F getFirst() {
        return this.first;
    }

    @Generated
    public S getSecond() {
        return this.second;
    }

    @Generated
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Pair)) {
            return false;
        }
        Pair<?, ?> other = (Pair<?, ?>) obj;
        if (this.first != null ? !this.first.equals(other.first) : other.first != null) {
            return false;
        }
        return this.second != null ? this.second.equals(other.second) : other.second == null;
    }

    @Generated
    public int hashCode() {
        int result = 1;
        result = result * 59 + (this.first != null ? this.first.hashCode() : 43);
        result = result * 59 + (this.second != null ? this.second.hashCode() : 43);
        return result;
    }

    @Generated
    public String toString() {
        return "Pair(first=" + this.first + ", second=" + this.second + ")";
    }
}