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
        return resolve<BasicTemplate, TemplateResolver.BasicInputData>(template, inputData)
    }

    override fun constructFullTemplate(
        template: String,
        inputData: TemplateResolver.FullInputData
    ): String {
        return resolve<FullTemplate, TemplateResolver.FullInputData>(template, inputData)
    }

    private inline fun <reified T : TypeSafeTemplate<D>, D> resolve(
        template: String,
        data: D
    ): String {
        return handlebars.compileInline(template).`as`(T::class.java).apply(data)
    }

    interface BasicTemplate : TypeSafeTemplate<TemplateResolver.BasicInputData>
    interface FullTemplate : TypeSafeTemplate<TemplateResolver.FullInputData>
}