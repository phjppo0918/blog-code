# 서론

# 본론
- dispatcher servlet의 간략한 설명 (클래스 주석)
```Actionscript
 * Central dispatcher for HTTP request handlers/controllers, e.g. for web UI controllers
 * or HTTP-based remote service exporters. Dispatches to registered handlers for processing
 * a web request, providing convenient mapping and exception handling facilities.
 *
 * <p>This servlet is very flexible: It can be used with just about any workflow, with the
 * installation of the appropriate adapter classes. It offers the following functionality
 * that distinguishes it from other request-driven web MVC frameworks:
 *
 * <ul>
 * <li>It is based around a JavaBeans configuration mechanism.
 *
 * <li>It can use any {@link HandlerMapping} implementation - pre-built or provided as part
 * of an application - to control the routing of requests to handler objects. Default is
 * {@link org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping} and
 * {@link org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping}.
 * HandlerMapping objects can be defined as beans in the servlet's application context,
 * implementing the HandlerMapping interface, overriding the default HandlerMapping if
 * present. HandlerMappings can be given any bean name (they are tested by type).
 *
 * <li>It can use any {@link HandlerAdapter}; this allows for using any handler interface.
 * Default adapters are {@link org.springframework.web.servlet.mvc.HttpRequestHandlerAdapter},
 * {@link org.springframework.web.servlet.mvc.SimpleControllerHandlerAdapter}, for Spring's
 * {@link org.springframework.web.HttpRequestHandler} and
 * {@link org.springframework.web.servlet.mvc.Controller} interfaces, respectively. A default
 * {@link org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter}
 * will be registered as well. HandlerAdapter objects can be added as beans in the
 * application context, overriding the default HandlerAdapters. Like HandlerMappings,
 * HandlerAdapters can be given any bean name (they are tested by type).
 *
 * <li>The dispatcher's exception resolution strategy can be specified via a
 * {@link HandlerExceptionResolver}, for example mapping certain exceptions to error pages.
 * Default are
 * {@link org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver},
 * {@link org.springframework.web.servlet.mvc.annotation.ResponseStatusExceptionResolver}, and
 * {@link org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver}.
 * These HandlerExceptionResolvers can be overridden through the application context.
 * HandlerExceptionResolver can be given any bean name (they are tested by type).
 *
 * <li>Its view resolution strategy can be specified via a {@link ViewResolver}
 * implementation, resolving symbolic view names into View objects. Default is
 * {@link org.springframework.web.servlet.view.InternalResourceViewResolver}.
 * ViewResolver objects can be added as beans in the application context, overriding the
 * default ViewResolver. ViewResolvers can be given any bean name (they are tested by type).
 *
 * <li>If a {@link View} or view name is not supplied by the user, then the configured
 * {@link RequestToViewNameTranslator} will translate the current request into a view name.
 * The corresponding bean name is "viewNameTranslator"; the default is
 * {@link org.springframework.web.servlet.view.DefaultRequestToViewNameTranslator}.
 *
 * <li>The dispatcher's strategy for resolving multipart requests is determined by a
 * {@link org.springframework.web.multipart.MultipartResolver} implementation.
 * An implementation for standard Servlet multipart processing is included.
 * The MultipartResolver bean name is "multipartResolver"; default is none.
 *
 * <li>Its locale resolution strategy is determined by a {@link LocaleResolver}.
 * Out-of-the-box implementations work via HTTP accept header, cookie, or session.
 * The LocaleResolver bean name is "localeResolver"; default is
 * {@link org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver}.
 *
 * <li>Its theme resolution strategy is determined by a {@link ThemeResolver}.
 * Implementations for a fixed theme and for cookie and session storage are included.
 * The ThemeResolver bean name is "themeResolver"; default is
 * {@link org.springframework.web.servlet.theme.FixedThemeResolver}.
 * Theme support is deprecated as of 6.0 with no direct replacement.
 * </ul>
 *
 * <p><b>NOTE: The {@code @RequestMapping} annotation will only be processed if a
 * corresponding {@code HandlerMapping} (for type-level annotations) and/or
 * {@code HandlerAdapter} (for method-level annotations) is present in the dispatcher.</b>
 * This is the case by default. However, if you are defining custom {@code HandlerMappings}
 * or {@code HandlerAdapters}, then you need to make sure that a corresponding custom
 * {@code RequestMappingHandlerMapping} and/or {@code RequestMappingHandlerAdapter}
 * is defined as well - provided that you intend to use {@code @RequestMapping}.
 *
 * <p><b>A web application can define any number of DispatcherServlets.</b>
 * Each servlet will operate in its own namespace, loading its own application context
 * with mappings, handlers, etc. Only the root application context as loaded by
 * {@link org.springframework.web.context.ContextLoaderListener}, if any, will be shared.
 *
 * <p>{@code DispatcherServlet} may be injected with a web application context,
 * rather than creating its own internally. This is useful in Servlet 3.0+
 * environments, which support programmatic registration of servlet instances.
 * See the {@link #DispatcherServlet(WebApplicationContext)} javadoc for details. 
```
요청 보냄 
    -> onRefresh(501)
        -> initStrategies(509)

```java
protected void initStrategies(ApplicationContext context) {
		initMultipartResolver(context);
		initLocaleResolver(context);
		initThemeResolver(context);
		initHandlerMappings(context);
		initHandlerAdapters(context);
		initHandlerExceptionResolvers(context);
		initRequestToViewNameTranslator(context);
		initViewResolvers(context);
		initFlashMapManager(context);
	}
```

multipartResolver -> null
LocaleResolver,
ThemeResolver,
initRequestToViewNameTranslator,
initFlashMapManager

-> DefaultStrategy()

initHandlerMappings()
모든 handlerMapping 를 감지(detect)할거면(default: true),
bean factory에서 HandlerMapping 타입인 모든 bean을가지고 온다
상위 컨텍스트를 포함하여 ApplicationContext에서 모든 HandlerMapping을 찾습니다,
아니면 ApplicationContext 내장된 default bean을 가지고 옵니다


initHandlerMappings() 와 비슷
initHandlerAdapters - HandlerAdapter
initHandlerExceptionResolvers - HandlerExceptionResolver,
initViewResolvers - ViewResolver

getDefaultStrategies -> 별도 properties 에서 가지고 온다


-> doService()

// Keep a snapshot of the request attributes in case of an include,
// to be able to restore the original attributes after the include.

request attribute를 스냅셧으로 저장하고, 이후 원본으로 복원할 수 있다
-> doDispatch() 실행 후, asyncManager가 동시 핸들링 처리를 시작하지 않았을 때 해당 스냅샷으로 복원한다

// Make framework objects available to handlers and view objects.

핸들러나 view 객체가 사용할 수 있도록 프레임워크 객체를 만든다

-> doDispatch()
```
	/**
	 * Process the actual dispatching to the handler.
	 * <p>The handler will be obtained by applying the servlet's HandlerMappings in order.
	 * The HandlerAdapter will be obtained by querying the servlet's installed HandlerAdapters
	 * to find the first that supports the handler class.
	 * <p>All HTTP methods are handled by this method. It's up to HandlerAdapters or handlers
	 * themselves to decide which methods are acceptable.
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @throws Exception in case of any kind of processing failure
	 */
```

multipart request 여부를 확인
현재 요청에 맞는 핸들러를 결정한다(determine) (doHandler())
현재 요청에 맞는 핸들러 adapter를 결정한다

//Process last-modified header, if supported by the handler

핸들러에서 지원하는 경우, 최종 수정 헤더를 처리한다
METHOD가 GET이거나 HEAD일 경우 속하는데, adapter의 lastModified를 가지고 오는데,
timeStamp를 확인하여 modify 여부를 확인한다



lastModified 같은 경우 -> 
```
	 * Same contract as for HttpServlet's {@code getLastModified} method.
	 * Can simply return -1 if there's no support in the handler class.
	 * @param request current HTTP request
	 * @param handler the handler to use
	 * @return the lastModified value for the given handler
	 * @deprecated as of 5.3.9 along with
```

adapter를 통해서 실제 핸들러를 호출한다

이후, asyncManager에서 동시핸들링 처리를 시작하면 그냥 종료한다

이후, default view Name 을 적용하고,
핸들러에게 이후 핸들 이후를 적용한다
mappedHandler.applyPostHandle(processedRequest, response, mv);

마지막으로,
만약 asyncManager에서 동시핸들링 처리를 시작하면
postHandle 과 afterCompletion 대신 핸들러에게 fterConcurrentHandlingStarted 를 적용한ㄷ
그리고 사용된 multipart request에 대해 정리한다

-> doHandler()
아까 init 했을 때 handlerMappings 중 적절한거 땡겨옴


응답 ->
processDispatchResult()
```
	/**
	 * Handle the result of handler selection and handler invocation, which is
	 * either a ModelAndView or an Exception to be resolved to a ModelAndView.
	 */

```

ModelAndView 에 해당 핸들러 선택 및 호출의 결과를 처리한다

handler와 modelAndView 상태가 문제가 없으면 render를 진행

render()
```
Render the given ModelAndView.
This is the last stage in handling a request. It may involve resolving the view by name.
```
// Determine locale for request and apply it to the response

요청에 대한 로케일을 설정하고, 이를 응답에 적용한다

view name을 설정

//Delegate to the View object for rendering
randering을 위해 view 객체에 위임