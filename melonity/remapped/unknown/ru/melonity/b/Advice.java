// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.b;

import lombok.Generated;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value = EnvType.CLIENT)
public final class Advice {
    private final String line;
    private final String description;

    @Generated
    public Advice(String line, String description) {
        this.line = line;
        this.description = description;
    }

    @Generated
    public String getLine() {
        return this.line;
    }

    @Generated
    public String getDescription() {
        return this.description;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Advice)) {
            return false;
        }
        Advice other = (Advice) o;
        if (this.line == null) {
            if (other.line != null) {
                return false;
            }
        } else if (!this.line.equals(other.line)) {
            return false;
        }
        if (this.description == null) {
            if (other.description != null) {
                return false;
            }
        } else if (!this.description.equals(other.description)) {
            return false;
        }
        return true;
    }

    @Generated
    public int hashCode() {
        int result = 1;
        result = result * 59 + (this.line == null ? 43 : this.line.hashCode());
        result = result * 59 + (this.description == null ? 43 : this.description.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "Advice(line=" + this.line + ", description=" + this.description + ")";
    }
}