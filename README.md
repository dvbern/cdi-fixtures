# cdi-fixtures

Fixture Framework to inject Testdata with CDI. You can define multiple configurators with different 
data for each entity.

## Getting Started

### Installation

* Checkout the repository
* run `mvn clean install`
* add the following dependency to your project

```xml
<dependency>
	<groupId>ch.dvbern.oss.cdifixtures</groupId>
	<artifactId>cdi-fixtures</artifactId>
	<version>(NEWEST_VERSION)</version>
</dependency>
```
### Usage

1. Create a Configurator

The configurator needs to implement the interface ch.dvbern.lib.cdi-fixtures.Configurator<T>.
In the configure()-method you can define the data needed for this specific configuration. 

2. Use it in Test

```
@Inject
@ConfiguratorClass(CustomerConfigurator.class)
@Persisted
private Instance<FixtureObject<Customer>> customerFixtureObject;
...

Customer testCustomer = customerFixtureObject.get().get();
```

3. Persisted vs. NonPersisted

Use the `@Persisted` Annotation if your entity should be persisted before your Test starts, or `@NonPersisted` if you
want to persist it in your Testcode.


## Built With

* [Maven](https://maven.apache.org/) - Dependency Management


## Contributing Guidelines

Please read [CONTRIBUTING.md](CONTRIBUTING.md) for the process for submitting pull requests to us.

## Code of Conduct

One healthy social atmospehere is very important to us, wherefore we rate our Code of Conduct high.
 For details check the file [CODE_OF_CONDUCT.md](CODE_OF_CONDUCT.md)

## Authors

* **DV Bern AG** - *Initial work* - [dvbern](https://github.com/dvbern)

See also the list of [contributors](https://github.com/dvbern/cdi-fixtures/contributors)
 who participated in this project.

## License

This project is licensed under the Apache 2.0 License - see the [License.md](LICENSE.md) file for details.

