package ru.kima.intelligentchat.di.domain.messaging

import com.github.jknack.handlebars.Handlebars
import com.github.jknack.handlebars.TypeSafeTemplate
import ru.kima.intelligentchat.domain.messaging.generation.prompting.TemplateResolver

class HandlebarsTemplateResolverImpl : TemplateResolver {
    private val handlebars = Handlebars()
    override fun constructBasicTemplate(
        template: String,
        inputData: TemplateResolver.BasicInputData
    ): String {
//        val context = Context
//            .newBuilder(inputData)
//            .resolver(
//                FieldValueResolver.INSTANCE,
//                JavaBeanValueResolver.INSTANCE
//            )
//            .build()
//        return handlebars.compileInline(template).apply(context)
        return resolve<BasicTemplate, TemplateResolver.BasicInputData>(template, inputData)
    }

    override fun constructFullTemplate(
        template: String,
        inputData: TemplateResolver.FullInputData
    ): String {
//        val context = Context
//            .newBuilder(inputData)
//            .resolver(
//                FieldValueResolver.INSTANCE,
//                JavaBeanValueResolver.INSTANCE
//            )
//            .build()
//        return handlebars.compileInline(template).apply(context)
        return resolve<FullTemplate, TemplateResolver.FullInputData>(template, inputData)
    }

    private inline fun <reified T : TypeSafeTemplate<D>, D> resolve(
        template: String,
        data: D
    ): String {
//        val context = Context.newBuilder(data).resolver(
//            FieldValueResolver.INSTANCE,
//            JavaBeanValueResolver.INSTANCE
//        ).build()
        return handlebars.compileInline(template)//.apply(context)
            .`as`(T::class.java).apply(data)
    }

    interface BasicTemplate : TypeSafeTemplate<TemplateResolver.BasicInputData>
    interface FullTemplate : TypeSafeTemplate<TemplateResolver.FullInputData>
}