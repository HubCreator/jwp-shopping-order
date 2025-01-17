package cart.domain.product;

import java.util.Objects;

public class ProductName {

    private final String name;

    public ProductName(final String name) {

        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProductName other = (ProductName) o;
        return Objects.equals(name, other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "ProductName{" +
                "name='" + name + '\'' +
                '}';
    }
}
